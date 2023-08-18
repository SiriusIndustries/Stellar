package sirius.stellar.facility.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public interface Transmittable extends Externalizable {

	@Override
	default void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {

	}

	@Override
	default void writeExternal(ObjectOutput output) throws IOException {

	}
}