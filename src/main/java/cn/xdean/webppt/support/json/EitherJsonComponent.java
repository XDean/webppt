package cn.xdean.webppt.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import xdean.jex.extra.collection.Either;

import java.io.IOException;

@JsonComponent
public class EitherJsonComponent {
    public static class Serializer extends JsonSerializer<Either<?, ?>> {
        @Override
        public void serialize(Either<?, ?> either, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (either.isLeft()) {
                jsonGenerator.writeObject(either.getLeft());
            } else {
                jsonGenerator.writeObject(either.getRight());
            }
        }
    }
}