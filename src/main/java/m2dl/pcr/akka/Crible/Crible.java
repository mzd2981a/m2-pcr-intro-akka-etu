package m2dl.pcr.akka.Crible;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import m2dl.pcr.akka.HelloGoodbyeWithParentActor.HelloActor;

public class Crible extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef cribleRef;
    private int num;
    private ActorRef prev;

    public Crible(int num) {
        log.info("Crible constructor with "+ num);
        this.num = num;
    }

    Procedure<Object> noChild = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof Integer) {
                int y = (Integer) msg;

                if(y%num != 0) {
                    cribleRef = getContext().actorOf(Props.create(Crible.class, y), "crible-actor-"+y);
                    cribleRef.tell(msg, getSelf());
                    log.info("Nombre premier : "+msg.toString());
                    prev = getSender();
                    getContext().become(wait,true);
                }else {
                    getSender().tell(new OK(), getSelf());
                }
            } else if(msg instanceof StopActor){
                log.info("Terminating...");
                getContext().stop(getSelf());
            }
            else {
                unhandled(msg);
                getSender().tell(new OK(), getSelf());

            }

        }
    };

    Procedure<Object> wait = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof OK) {
                prev.tell(new OK(), getSelf());
                getContext().become(withChild);

            }
            else {
                unhandled(msg);
            }

        }
    };



    Procedure<Object> withChild = new Procedure<Object>() {
        public void apply(Object msg) throws Exception {
            if (msg instanceof Integer) {
                int y = (Integer) msg;
                if(y%num != 0) {
                    cribleRef.tell(msg, getSelf());
                    prev = getSender();
                    getContext().become(wait,true);
                } else {
                    getSender().tell(new OK(), getSelf());
                }
            } else if(msg instanceof StopActor){
                log.info("Terminating...");
                cribleRef.tell(new StopActor(), getSelf());
                getContext().stop(getSelf());

            }

            else {
                unhandled(msg);
                getSender().tell(new OK(), getSelf());

            }

        }
    };



    @Override
    public void onReceive(Object o) throws Exception {
        noChild.apply(o);

    }
}
