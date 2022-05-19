package m2dl.pcr.akka.HelloGoodbyeWithParentActor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GoodbyeActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public GoodbyeActor() {
        log.info("HelloActor constructor");
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof String) {
            log.info("Goodbye " + msg + "!");
        } else {
            unhandled(msg);
        }
    }
}
