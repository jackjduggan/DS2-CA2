package com.hbjdjd.ca2.Map;

import java.util.LinkedList;

public interface PathFindAlgo {

    /**
     * Compute most efficient path.
     * @param mapOverlay
     * @param start
     * @param end
     * @return The most efficient path.
     */
    public Path computePath(MapOverlay mapOverlay, Node start, Node end);

    /**
     * Compute multiple paths.
     * @param mapOverlay
     * @param start
     * @param end
     * @return All possible paths, in order of efficiency.
     */
    public LinkedList<Path> computePaths(MapOverlay mapOverlay, Node start, Node end);
}
