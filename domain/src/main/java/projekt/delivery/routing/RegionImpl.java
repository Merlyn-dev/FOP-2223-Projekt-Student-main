package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

class RegionImpl implements Region {

    private final Map<Location, NodeImpl> nodes = new HashMap<>();
    private final Map<Location, Map<Location, EdgeImpl>> edges = new HashMap<>();
    private final List<EdgeImpl> allEdges = new ArrayList<>();
    private final DistanceCalculator distanceCalculator;

    /**
     * Creates a new, empty {@link RegionImpl} instance using a {@link EuclideanDistanceCalculator}.
     */
    public RegionImpl() {
        this(new EuclideanDistanceCalculator());
    }

    /**
     * Creates a new, empty {@link RegionImpl} instance using the given {@link DistanceCalculator}.
     */
    public RegionImpl(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public @Nullable Node getNode(Location location) {
        return nodes.get(location);
    }

    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        Edge edge = edges.getOrDefault(locationA, Collections.emptyMap()).get(locationB);
        //
        if (edge == null) {
            edge = edges.getOrDefault(locationB, Collections.emptyMap()).get(locationA);
        }
        return edge;
    }

    @Override
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    @Override
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableList(allEdges);
    }

    @Override
    public DistanceCalculator getDistanceCalculator() {
        return distanceCalculator;
    }

    /**
     * Adds the given {@link NodeImpl} to this {@link RegionImpl}.
     * @param node the {@link NodeImpl} to add.
     */
    void putNode(NodeImpl node) {
        if (!this.equals(node.getRegion())) {
            throw new IllegalArgumentException("Node " + node + " has incorrect region"); //not in the map
        }
        nodes.put(node.getLocation(), node); //only if no exception is thrown
    }

    /**
     * Adds the given {@link EdgeImpl} to this {@link RegionImpl}.
     * @param edge the {@link EdgeImpl} to add.
     */
    void putEdge(EdgeImpl edge) {
        // check if the edge belongs to this region
        if (!this.equals(edge.getRegion())) {
            throw new IllegalArgumentException("Edge " + edge + " has incorrect region");
        }
        // check if both nodes of the edge are part of this region
        if (!nodes.containsValue(edge.getNodeA()) || !nodes.containsValue(edge.getNodeB())) {
            throw new IllegalArgumentException("Edge " + edge + " has incorrect region");
        }
        // check if both nodes of the edge are not null
        if (edge.getNodeA() == null) {
            throw new IllegalArgumentException("NodeA " + edge.getNodeA() + " is not part of the region");
        }
        if (edge.getNodeB() == null) {
            throw new IllegalArgumentException("NodeB " + edge.getNodeB() + " is not part of the region");
        }
        // add the edge to the two-dimensional map
        Location locA = edge.getNodeA().getLocation();
        Location locB = edge.getNodeB().getLocation();
        edges.computeIfAbsent(locA, k -> new HashMap<>()).put(locB, edge);
        edges.computeIfAbsent(locB, k -> new HashMap<>()).put(locA, edge);
        // add the edge to the one-dimensional list and maintain the sorting
        int i = Collections.binarySearch(allEdges, edge);
        if (i < 0) {
            i = -(i + 1);
        }
        allEdges.add(i, edge);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) { //identical?
            return true;
        }
        if (!(o instanceof RegionImpl)) { //passed object is of type RegionImpl or a subtype?
            return false;
        }
        return Objects.equals(this.nodes, ((RegionImpl) o).nodes) && Objects.equals(this.edges, ((RegionImpl) o).edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }
}
