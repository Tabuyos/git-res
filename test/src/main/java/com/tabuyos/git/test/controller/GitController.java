package com.tabuyos.git.test.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Description:
 *
 * <pre>
 *   <b>project: </b><i>git-res</i>
 *   <b>package: </b><i>com.tabuyos.git.test.controller</i>
 *   <b>class: </b><i>GitController</i>
 *   comment here.
 * </pre>
 *
 * @author
 *     <pre><b>username: </b><i><a href="http://www.tabuyos.com">Tabuyos</a></i></pre>
 *     <pre><b>site: </b><i><a href="http://www.tabuyos.com">http://www.tabuyos.com</a></i></pre>
 *     <pre><b>email: </b><i>tabuyos@outlook.com</i></pre>
 *     <pre><b>description: </b><i>
 *   <pre>
 *     Talk is cheap, show me the code.
 *   </pre>
 * </i></pre>
 *
 * @version 0.1.0
 * @since 0.1.0 - 2/1/21 4:24 PM
 */
@RestController
@RequestMapping("/api/git")
public class GitController {

  private static final Logger console = LoggerFactory.getLogger(GitController.class);

  @Autowired
  private GitProperties properties;

  @RequestScope
  @PostMapping("/create/{projectName}")
  public ResponseEntity createProject(
      @PathVariable("projectName") String projectName,
      @RequestBody(required = false) GitCommit commitModel) {
    try {

      Repository repository = properties.getRepository(projectName);
      //            created git repository
      repository.create();
      Git gitCommand = new Git(repository);

      //            list folder created
      List<File> folders = properties.createFolder(projectName, Arrays.asList("SIT", "UIT"));

      //            git add .
      gitCommand.add().addFilepattern(".").call();

      //            git commit -m "init project"
      CommitCommand commitCommand =
          gitCommand
              .commit()
              .setMessage("init project")
              .setCommitter(commitModel.getUsername(), commitModel.getEmail());
      commitCommand.call();

      //            git log
      Iterable<RevCommit> listLog = gitCommand.log().setMaxCount(1).call();
      List<GitCommit> refObject = new ArrayList<>();
      for (RevCommit log : listLog) {
        GitCommit commit = new GitCommit();
        commit.setId(log.getId().getName());
        commit.setMessage(log.getFullMessage());
        commit.setUsername(log.getCommitterIdent().getName());
        commit.setEmail(log.getCommitterIdent().getEmailAddress());
        refObject.add(commit);
      }
      repository.close();
      return new ResponseEntity(refObject.get(0), HttpStatus.CREATED);
    } catch (java.lang.IllegalStateException ilste) {
      return new ResponseEntity(HttpStatus.CONFLICT);
    } catch (GitAPIException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
  }

  @GetMapping("/directories/{projectName}")
  public ResponseEntity listDirectory(@PathVariable("projectName") String projectName) {
    List<Files> directories = new ArrayList<>();
    String basePath = properties.getBaseDirectory(projectName);
    System.out.println(basePath);
    File directory = new File(basePath);
    File[] files = directory.listFiles();
    for (File dir : files) {
      if (!dir.getName().equalsIgnoreCase(".git") && dir.isDirectory()) {
        directories.add(new Files(dir.getName(), dir.isDirectory(), dir.getPath()));
      }
    }
    System.out.println(directories);
    if (directories.isEmpty()) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity(directories, HttpStatus.OK);
    }
  }

  @GetMapping("/files/{projectName}")
  public ResponseEntity listFiles(@PathVariable("projectName") String projectName) {
    List<Files> files = new ArrayList<>();
    String basePath = properties.getBaseDirectory(projectName);
    File file = new File(basePath);
    File[] tempFiles = file.listFiles();
    for (File aFile : tempFiles) {
      if (!aFile.getName().equalsIgnoreCase(".git")) {
        files.add(new Files(aFile.getName(), aFile.isDirectory(), aFile.getPath()));
      }
    }

    if (files.isEmpty()) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity(files, HttpStatus.OK);
    }
  }

  @GetMapping("/logs/{projectName}")
  public ResponseEntity listLog(@PathVariable("projectName") String projectName) {
    List<GitLog> listLog = new ArrayList<>();
    try {
      Repository repository = properties.getRepository(projectName);
      Git gitCommand = new Git(repository);
      Iterable<RevCommit> logs = gitCommand.log().call();
      for (RevCommit log : logs) {
        StringBuilder sb = new StringBuilder();

        console.info("{}", log.getFooterLines());
        listLog.add(
            new GitLog(
                log.toObjectId().getName(),
                log.getCommitterIdent().getName(),
                log.getCommitterIdent().getEmailAddress(),
                log.toString(),
                log.getFullMessage()));
      }

      repository.close();
      return new ResponseEntity(listLog, HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (NoHeadException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (GitAPIException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/diff/{projectName}")
  public ResponseEntity listDiff(
      @PathVariable("projectName") String projectName,
      @RequestParam(name = "oldCommit") String oldCommit,
      @RequestParam(name = "newCommit") String newCommit) {
    try {
      Repository repository = properties.getRepository(projectName);
      Git gitCommand = new Git(repository);
      List<DiffEntry> diffEntries = listDiff(repository, gitCommand, oldCommit, newCommit);
      StringBuilder sb = new StringBuilder();
      for (DiffEntry entry : diffEntries) {
        sb.append(entry.getChangeType().toString())
            .append(" : ")
            .append(
                entry.getOldPath().equals(entry.getNewPath())
                    ? entry.getNewPath()
                    : entry.getOldPath() + " -> " + entry.getNewPath());

        OutputStream output =
            new OutputStream() {
              StringBuilder builder = new StringBuilder();

              @Override
              public void write(int b) throws IOException {
                builder.append((char) b);
              }

              @Override
              public String toString() {
                return this.builder.toString();
              }
            };

        try (DiffFormatter formatter = new DiffFormatter(output)) {
          formatter.setRepository(repository);
          formatter.format(entry);
        }
        sb.append("\n").append(output.toString());
      }
      return new ResponseEntity(sb.toString(), HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.CONFLICT);
    } catch (GitAPIException e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  private static List<DiffEntry> listDiff(
      Repository repository, Git git, String oldCommit, String newCommit)
      throws GitAPIException, IOException {
    final List<DiffEntry> diffs =
        git.diff()
            .setOldTree(prepareTreeParser(repository, oldCommit))
            .setNewTree(prepareTreeParser(repository, newCommit))
            .call();

    System.out.println("Found: " + diffs.size() + " differences");
    for (DiffEntry diff : diffs) {
      System.out.println(
          "Diff: "
              + diff.getChangeType()
              + ": "
              + (diff.getOldPath().equals(diff.getNewPath())
                  ? diff.getNewPath()
                  : diff.getOldPath() + " -> " + diff.getNewPath()));
    }
    return diffs;
  }

  private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId)
      throws IOException {
    // from the commit we can build the tree which allows us to construct the TreeParser
    //noinspection Duplicates
    try (RevWalk walk = new RevWalk(repository)) {
      RevCommit commit = walk.parseCommit(repository.resolve(objectId));
      RevTree tree = walk.parseTree(commit.getTree().getId());

      CanonicalTreeParser treeParser = new CanonicalTreeParser();
      try (ObjectReader reader = repository.newObjectReader()) {
        treeParser.reset(reader, tree.getId());
      }

      walk.dispose();

      return treeParser;
    }
  }
}
