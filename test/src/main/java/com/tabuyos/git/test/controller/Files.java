package com.tabuyos.git.test.controller;

/**
 * <p>Description: </p>
 * <pre>
 *   <b>project: </b><i>git-res</i>
 *   <b>package: </b><i>com.tabuyos.git.test.controller</i>
 *   <b>class: </b><i>Files</i>
 *   comment here.
 * </pre>
 *
 * @author <pre><b>username: </b><i><a href="http://www.tabuyos.com">Tabuyos</a></i></pre>
 * <pre><b>site: </b><i><a href="http://www.tabuyos.com">http://www.tabuyos.com</a></i></pre>
 * <pre><b>email: </b><i>tabuyos@outlook.com</i></pre>
 * <pre><b>description: </b><i>
 *   <pre>
 *     Talk is cheap, show me the code.
 *   </pre>
 * </i></pre>
 * @version 0.1.0
 * @since 0.1.0 - 2/1/21 6:15 PM
 */
public class Files {

  public Files() {
  }

  public Files(String fileName, boolean folder, String path) {
    this.fileName = fileName;
    this.folder = folder;
    this.path = path;
  }

  private String fileName;
  private boolean folder;
  private String path;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public boolean isFolder() {
    return folder;
  }

  public void setFolder(boolean folder) {
    this.folder = folder;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return "Files{" +
        "fileName='" + fileName + '\'' +
        ", folder=" + folder +
        ", path='" + path + '\'' +
        '}';
  }
}
