package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

class NodeImpl implements Region.Node {

    protected final Set<Location> connections;
    protected final Region region;
    protected final String name;
    protected final Location location;

    /**
     * Creates a new {@link NodeImpl} instance.
     * @param region The {@link Region} this {@link NodeImpl} belongs to.
     * @param name The name of this {@link NodeImpl}.
     * @param location The {@link Location} of this {@link EdgeImpl}.
     * @param connections All {@link Location}s this {@link NeighborhoodImpl} has an {@link Region.Edge} to.
     */
    NodeImpl(
        Region region,
        String name,
        Location location,
        Set<Location> connections
    ) {
        this.region = region;
        this.name = name;
        this.location = location;
        this.connections = connections;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public Set<Location> getConnections() {
        return connections;
    }

    @Override
    public @Nullable Region.Edge getEdge(Region.Node other) {
        for (Location connection : connections) {
            // get edges between current node and connection
            Region.Edge edge = region.getEdge(this, other);
            if (edge != null && (edge.getNodeB() == other || edge.getNodeA() == other)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        Set<Region.Node> adjacentNodes = new HashSet<>();

        return adjacentNodes;
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {
        Set<Region.Edge> adjacentEdges = new HashSet<>();

        return adjacentEdges;
    }

    @Override
    public int compareTo(Region.Node o) {
         return location.compareTo(o.getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { //identical?
            return true;
        }
        if (o == null || getClass() != o.getClass()) { //null or not the correct data type
            return false;
        }
        return Objects.equals(name, ((NodeImpl) o).name) && Objects.equals(location, ((NodeImpl) o).location) && Objects.equals(connections, ((NodeImpl) o).connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, connections);
    }

    @Override
    public String toString() {
        return "NodeImpl(name='" + name + "', location='" + location + "', connections='" + connections + "')";
    }
}
