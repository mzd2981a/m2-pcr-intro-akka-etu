package m2dl.pcr.akka.HelloGoodbyeWithParentActor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import m2dl.pcr.akka.helloworld2.NameActor;

public class ParentActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef helloRef;
    private ActorRef goodbyeRef;

    public ParentActor() {

        log.info("ParentActor constructor");
        helloRef = getContext().actorOf(Props.create(HelloActor.class), "hello-actor");
        goodbyeRef = getContext().actorOf(Props.create(GoodbyeActor.class), "goodbye-actor");
    }


    Procedure<Object> hello = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof String) {
                helloRef.tell(msg, getSelf());
                getContext().become(goodbye,false);
            } else {
                unhandled(msg);
            }
        }
    };

    Procedure<Object> goodbye = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof String) {
                goodbyeRef.tell(msg, getSelf());
                getContext().unbecome();
            } else {
                unhandled(msg);
            }
        }
    };

    @Override
    public void onReceive(Object o) throws Exception {
        hello.apply(o);
    }
}
