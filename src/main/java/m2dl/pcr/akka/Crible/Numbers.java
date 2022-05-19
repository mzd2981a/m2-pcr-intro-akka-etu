package m2dl.pcr.akka.Crible;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class Numbers extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    ActorRef firstCrible;
    private int currentNum;

    public Numbers() {
        firstCrible = getContext().actorOf(Props.create(Crible.class, 2), "first-crible");
    }

    Procedure<Object> send = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof Integer) {
                currentNum = (Integer) msg;
                firstCrible.tell(currentNum, getSelf());
                getContext().become(wait,true);
            } else {
                unhandled(msg);
            }
        }
    };

    Procedure<Object> wait = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof OK) {
                if(currentNum<30) {
                    currentNum++;
                    firstCrible.tell(currentNum, getSelf());
                }else {
                    firstCrible.tell(new StopActor(), getSelf());
                    getContext().stop(getSelf());
                }

            } else {
                unhandled(msg);
            }
        }
    };

    @Override
    public void onReceive(Object o) throws Exception {
        send.apply(o);
    }
}
