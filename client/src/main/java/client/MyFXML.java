package client;

import client.scenes.EventPageCtrl;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MyFXML {

    private Injector injector;
    private Map<Class<?>, Object> controllers = new HashMap<>();

    public MyFXML(Injector injector) {
        this.injector = injector;
    }

    public <T> Pair<T, Parent> load(Class<T> c, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), null, null,
                    new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            controllers.put(c, ctrl); // Store the controller instance
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Pair<T, Parent> load(Class<T> c, ResourceBundle messages,
                                    Locale locale, String... parts) {
        try {
            var loader = new FXMLLoader(getLocation(parts), messages, null,
                    new MyFactory(), StandardCharsets.UTF_8);
            Parent parent = loader.load();
            T ctrl = loader.getController();
            controllers.put(c, ctrl); // Store the controller instance
            return new Pair<>(ctrl, parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }

    public EventPageCtrl getController(Class<EventPageCtrl> eventPageCtrlClass) {
        return (EventPageCtrl) controllers.get(eventPageCtrlClass);
    }

    private class MyFactory implements BuilderFactory, Callback<Class<?>, Object> {

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<?> getBuilder(Class<?> type) {
            return new Builder() {
                @Override
                public Object build() {
                    return injector.getInstance(type);
                }
            };
        }

        @Override
        public Object call(Class<?> type) {
            return injector.getInstance(type);
        }
    }
}
