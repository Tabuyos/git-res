package com.tabuyos.git.test.controller;

/**
 * <p>Description: </p>
 * <pre>
 *   <b>project: </b><i>git-res</i>
 *   <b>package: </b><i>com.tabuyos.git.test.controller</i>
 *   <b>class: </b><i>GitLog</i>
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
public class GitLog {

  public GitLog() {
  }

  public GitLog(String id, String username, String email, String body, String message) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.body = body;
    this.message = message;
  }

  private String id;
  private String username;
  private String email;
  private String body;
  private String message;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "GitLog{" +
        "id='" + id + '\'' +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", body='" + body + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
