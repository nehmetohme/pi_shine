package ch.abtomik.kura.example.shine_osgi;
import java.io.IOException;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import static org.osgi.service.component.annotations.ConfigurationPolicy.REQUIRE;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


@Designate(ocd = Config.class)
@Component(immediate = true, configurationPolicy = REQUIRE)

public class ShineOsgi implements ConfigurableComponent {

    public GpioController gpioController;
    public GpioPinDigitalOutput pinOut;

    private static final Logger s_logger = LoggerFactory.getLogger(ShineOsgi.class);

    private static final String APP_ID = "ch.abtomik.kura.example.shime_osgi";

    @Activate
    public void activate() {
        s_logger.info("enter activate");
        System.out.println("activate nemo");
        turnOn("activate");
    }

    @Deactivate
    public void deactivate() {

        s_logger.info("Bundle " + APP_ID + " has stopped!");
        System.out.println("deactivate");

    }

    private void turnOn(String operation){
        s_logger.info("Bundle " + APP_ID + " has started!");
        s_logger.info("GPIO Control Example");
        
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);

        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);

        System.out.println("--> GPIO state should be: ON");

        try {
            Thread.sleep(10000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // turn off gpio pin #01
        pin.low();
        System.out.println("--> GPIO state should be: OFF");

        gpio.shutdown();
	gpio.unprovisionPin(pin);

        System.out.println("Exiting ControlGpioExample");

    }
}
