package uk.me.graphe.client;

import uk.me.graphe.shared.VertexDirection;

public class EdgeDrawable {
    private int mStartX;
    private int mStartY;

    private int mEndX;
    private int mEndY;
    

    /**
     * determines of the edge needs an arrow
     * 
     * arrows are always at the end edge
     */
    private VertexDirection mArrowDirection;
    private boolean mHilighted;
    private int mWeight;

    /**
     * creates a new EdgeDrawable, with no arrow
     * 
     * @param startX
     *            the start x of the edge
     * @param startY
     *            the start y of the edge
     * @param endX
     *            the end x of the edge
     * @param endY
     *            the end y of the edge
     */
    public EdgeDrawable(int startX, int startY, int endX, int endY, int weight) {
        this(startX, startY, endX, endY, weight, VertexDirection.both);
    }

    /**
     * creates a new EdgeDrawable
     * 
     * @param startX
     *            the start x of the edge
     * @param startY
     *            the start y of the edge
     * @param endX
     *            the end x of the edge
     * @param endY
     *            the end y of the edge
     * @param arrowDir
     *            the direction in which the arrow should be drawn
     */
    public EdgeDrawable(int startX, int startY, int endX, int endY, int weight, VertexDirection arrowDir) {
        mStartX = startX;
        mStartY = startY;

        mEndX = endX;
        mEndY = endY;
        mWeight = weight;
        mArrowDirection = arrowDir;
    }

    /**
     * gets the x of the start of the line
     * 
     * @return int representing x at the start of the line
     */
    public int getStartX() {
        return mStartX;
    }

    /**
     * gets the y of the start of the line
     * 
     * @return int representing y at the start of the line
     */
    public int getStartY() {
        return mStartY;
    }

    /**
     * gets the x of the end of the line
     * 
     * @return int represehnting x at the end of the line
     */
    public int getEndX() {
        return mEndX;
    }

    /**
     * gets the y at the end of the line
     * 
     * @return int representing y at the end of the line
     */
    public int getEndY() {
        return mEndY;
    }
    
    /**
     * determines if a ed contains a specific co-ordinate 
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y) {
        return false;
    }
    
    
    boolean in_or_out_of_polygon(int X[], int Y[], int x, int y)
    {
        int i, j;
        boolean c = false;
        for (i = 0, j = X.length-2; i < X.length-1; j = i++)
        {
    
        if (( ((Y[i]<=y) && (y<Y[j])) || ((Y[j]<=y) && (y<Y[i])) ) &&
                (x < (X[j]-X[i]) * (y-Y[i]) / (Y[j]-Y[i]) + X[i]))
            c = !c;
        }
        return c;
    }

    /**
     * determines if the line needs an arrow in the fromTo direction
     * 
     * @return true if the line needs an arrow in the fromTo direction
     */
    public boolean needsFromToArrow() {
        return mArrowDirection == VertexDirection.fromTo;
    }
    
    public boolean needsToFromArrow() {
        return mArrowDirection == VertexDirection.toFrom;
    }
    
    public int getWeight() {
        return mWeight;
    }
    
    public boolean isHilighted() {
        return mHilighted;
    }
    
    public void setHilighted(boolean h) {
        mHilighted = h;
    }
    
    public void setStartX(int x) {
        mStartX = x;
    }

    public void setStartY(int y) {
        mStartY = y;
    }

    public void setEndX(int x) {
        mEndX = x;
    }
    
    public void setEndY(int y) {
        mEndY = y;
    }
    
    
}
