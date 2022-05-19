package m2dl.pcr.akka.Crible;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import m2dl.pcr.akka.HelloGoodbyeWithParentActor.ParentActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class System {


    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("actor-system");

        Thread.sleep(5000);

        final ActorRef actorRef = actorSystem.actorOf(Props.create(Numbers.class), "numbers");

        actorRef.tell(3, null);


        Thread.sleep(5000);


        log.debug("Actor System Shutdown Starting...");

        actorSystem.terminate();
    }
}
