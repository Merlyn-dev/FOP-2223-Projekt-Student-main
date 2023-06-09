package projekt.delivery.service;

import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A very simple delivery service that distributes orders to compatible vehicles in a FIFO manner.
 */
public class BasicDeliveryService extends AbstractDeliveryService {

    // List of orders that have not yet been loaded onto delivery vehicles
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public BasicDeliveryService(
        VehicleManager vehicleManager
    ) {
        super(vehicleManager);
    }

    @Override
    protected List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {
        List<Event> returnList = tick(currentTick); //create returnList
        Iterator<ConfirmedOrder> it = newOrders.iterator(); //create iterator for confirmedOrder
        //add new orders:
        while (it.hasNext()) {
            pendingOrders.add(it.next());
        }
        //sort confirmedOrder list
        ConfirmedOrder[] ordersArray = new ConfirmedOrder[pendingOrders.size()];
        for (int i=0; i<pendingOrders.size(); i++) {
            ordersArray[i] = pendingOrders.get(i);
        }
        for (int j=0; j <= pendingOrders.size(); j++) {
            for (int k=0; k<= pendingOrders.size(); k++) {
                if (ordersArray[j].getActualDeliveryTick() > ordersArray[k].getActualDeliveryTick()) {
                    ConfirmedOrder newJ = ordersArray[k];
                    ConfirmedOrder newK = ordersArray[j];
                    ordersArray[k] = newK;
                    ordersArray[j] = newJ;
                }
            }
        }
        return returnList;
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return pendingOrders;
    }

    @Override
    public void reset() {
        super.reset();
        pendingOrders.clear();
    }

    public interface Factory extends DeliveryService.Factory {

        BasicDeliveryService create(VehicleManager vehicleManager);
    }
}
