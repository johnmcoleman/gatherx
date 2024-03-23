package model;

public class GraphMessage {
  private Graph graph;
  private String message;
  private boolean state;

  public GraphMessage(Graph newGraph, String newMessage, boolean newState) {
    graph = newGraph;
    message = newMessage;
    state = newState;
  }

  public Graph getGraph() {
    return graph;
  }

  public void setGraph(Graph graph) {
    this.graph = graph;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean getState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

}
