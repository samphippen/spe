package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.client.communications.ReceiveNotificationRunner;
import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.json.wrapper.JSOFactory;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.graphmanagers.GraphManager2dFactory;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.HeartbeatMessage;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Graphemeui implements EntryPoint {

    public Toolbox tools;
    public Canvas canvas;
    public GraphManager2dFactory graphManagerFactory;
    public GraphManager2d graphManager;
    public int nodes;
    public static final int VERTEX_SIZE = 10;
    private Timer mHeartbeatTimer;
    private int mHeartbeats = 0;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Graphemeui gui = new Graphemeui();
        JSONImplHolder.initialise(new JSOFactory());
        RootPanel.get("toolbox").add(gui.getToolBox());
        RootPanel.get("canvas").add(gui.getCanvas());
        ServerChannel sc = ServerChannel.getInstance();
        sc.init();
        sc.addReceiveNotification(new ReceiveNotificationRunner() {

            @Override
            public void run(String s) {
                List<JSONObject> jsos = new ArrayList<JSONObject>();
                int startIndex = 0;
                int endIndex;
                do {

                    endIndex = s.indexOf("\0", startIndex);
                    if (endIndex == -1) endIndex = s.length();
                    try {
                        jsos.add(JSONImplHolder.make(s.substring(startIndex,
                                endIndex)));
                    } catch (JSONException e) {
                        Window.alert("bees");
                    }
                    startIndex = endIndex + 1;

                } while (s.indexOf("\0", startIndex) != -1);
                try {
                    List<Message> messages = MessageFactory.makeOperationsFromJson(jsos);
                    
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        mHeartbeatTimer = new Timer() {

            @Override
            public void run() {
                ServerChannel.getInstance().send(
                        new HeartbeatMessage().toJson());
            }
        };

        mHeartbeatTimer.scheduleRepeating(500);

    }

    private Widget getToolBox() {
        return tools;
    }

    private Widget getCanvas() {
        return canvas;
    }

    public Graphemeui() {
        nodes = 1;
        tools = new Toolbox(this);
        canvas = new Canvas(this);
        graphManagerFactory = GraphManager2dFactory.getInstance();
        graphManager = graphManagerFactory.makeDefaultGraphManager();
        final Drawing d = new DrawingImpl();
        graphManager.addRedrawCallback(new Runnable() {

            @Override
            public void run() {
                d.renderGraph(canvas.canvasPanel, graphManager
                        .getEdgeDrawables(), graphManager.getVertexDrawables());// graph
                // goes
                // here!
            }
        });

    }

    /**
     * Called by child canvas onClick(), takes in starting coordinates and the
     * end coordinates used if there was a drag. If options are required brings
     * up a dialog box in the options stack.
     */
    public void initOptions(int x1, int y1, int x2, int y2) {
        int tool = tools.getTool();
        if (tool == 1) {
            // add node tool
            EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(),
                    3, this);
            ed.setPoint(x1, y1);
            tools.getOptionsPanel().add(ed);
        } else if (tool == 2) {
            // add edge tool
            EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(),
                    0, this);
            tools.getOptionsPanel().add(ed);
        } else if (tool == 3) {
            // remove node tool
            EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(),
                    1, this);
            tools.getOptionsPanel().add(ed);
        } else if (tool == 4) {
            // remove edge tool
            EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(),
                    2, this);
            tools.getOptionsPanel().add(ed);
        }
    }

    /**
     * called by options dialog box by OK button's onClick()
     * 
     * @param type
     *            - the type of element to add (0 = edge, 1 = remove vertex, 2 =
     *            remove edge, 3 = add vertex)
     * @param v1
     *            - the vertex to remove/add to
     * @param v2
     *            - the end vertex (if adding an edge)
     * @param edge
     *            - the edge to remove
     * @param ed
     *            - the dialog box itself (passed in so we can delete it
     *            afterwards)
     */
    public void addElement(int type, int v1, int v2, int edge, String label,
            EdgeDialog ed) {
        if (type == 2) {
            graphManager.removeEdge(graphManager.getUnderlyingGraph()
                    .getEdges().get(edge));
        } else if (type == 1) {
            graphManager.removeVertex(graphManager.getUnderlyingGraph()
                    .getVertices().get(v1));
        } else if (type == 0) {
            graphManager.addEdge(graphManager.getUnderlyingGraph()
                    .getVertices().get(v1), graphManager.getUnderlyingGraph()
                    .getVertices().get(v2), VertexDirection.fromTo);
        } else if (type == 3) {
            Vertex v = new Vertex(label);
            int[] coords = ed.getPoint();
            graphManager.addVertex(v, coords[0], coords[1], VERTEX_SIZE);
        }
        removeOptions(ed);
    }

    public void removeOptions(EdgeDialog ed) {
        tools.getOptionsPanel().remove(ed);
    }

}
