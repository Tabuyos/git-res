package com.tabuyos.git.test.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * <pre>
 *   <b>project: </b><i>git-res</i>
 *   <b>package: </b><i>com.tabuyos.git.test.controller</i>
 *   <b>class: </b><i>GitProperties</i>
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
 * @since 0.1.0 - 2/1/21 6:11 PM
 */
@Component
public class GitProperties {

  private String uriRepository(String project) {
    return new StringBuilder(getBaseDirectory(project))
        .append(File.separator)
        .append(".git")
        .toString();
  }

  public String getBaseDirectory(String projectName) {
    return new StringBuilder(System.getProperty("user.home"))
        .append(File.separator)
        .append("tabuyos")
        .append(File.separator)
        .append("git-home")
        .append(File.separator)
        .append(projectName)
        .toString();
  }

  public File getBaseGitDir(String projectName) {
    return new File(uriRepository(projectName));
  }

  public List<File> createFolder(String projectName, List<String> folderNames) throws IOException {
    List<File> gitkeep = new ArrayList<>();
    for (String name : folderNames) {
      String folderLocation =
          new StringBuilder(getBaseDirectory(projectName))
              .append(File.separator)
              .append(name)
              .toString();
      File folder = new File(folderLocation);
      boolean dir = folder.mkdirs();
      if (dir) {
        File gitKeep =
            new File(
                new StringBuilder(folderLocation)
                    .append(File.separator)
                    .append(".gitkeep")
                    .toString());
        gitKeep.createNewFile();
        gitkeep.add(gitKeep);
      }
    }
    return gitkeep;
  }

  public String uriAbsoluteRepository(String project) {
    return new StringBuilder(System.getProperty("user.home"))
        .append(File.separator)
        .append(project)
        .toString();
  }

  public Repository getRepository(String projectName) throws IOException {
    return new FileRepositoryBuilder()
        .setGitDir(getBaseGitDir(projectName))
        .readEnvironment()
        .setup()
        .findGitDir()
        .build();
  }
}
