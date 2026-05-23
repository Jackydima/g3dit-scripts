package de.george.g3dit.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.google.common.collect.ImmutableSet;

import de.javakaffee.kryoserializers.guava.ImmutableSetSerializer;

public abstract class KryoInstance {
	private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo(new InterfaceAwareClassResolver(), null);
			kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
			kryo.addDefaultSerializer(Path.class, new Serializer<Path>() {
				@Override
				public void write(Kryo kryo, Output output, Path object) {
					output.writeString(object.toString());
				}

				@Override
				public Path read(Kryo kryo, Input input, Class<? extends Path> type) {
					return Paths.get(input.readString());
				}
			});
			ImmutableSetSerializer.registerSerializers(kryo);
			kryo.setRegistrationRequired(true);
			kryo.register(com.google.common.collect.ImmutableSet.class, 9);
			kryo.register(de.george.g3dit.cache.EntityCache.EntityCacheEntry.class, 10);
			kryo.register(de.george.g3dit.cache.LightCache.LightSource.class, 11);
			kryo.register(de.george.g3dit.cache.TemplateCache.TemplateCacheEntry.class, 12);
			kryo.register(de.george.g3dit.check.FileDescriptor.class, 13);
			kryo.register(de.george.g3dit.check.FileDescriptor.FileType.class, 14);
			kryo.register(de.george.g3utils.structure.bCMatrix.class, 15);
			kryo.register(de.george.g3utils.structure.bCVector.class, 16);
			kryo.register(de.george.g3utils.structure.bCVector4.class, 17);
			kryo.register(de.george.lrentnode.structures.bCFloatColor.class, 18);
			kryo.register(de.george.navmap.data.NavPath.class, 19);
			kryo.register(de.george.navmap.data.NavPath.ZonePathIntersection.class, 20);
			kryo.register(de.george.navmap.data.NavZone.class, 21);
			kryo.register(java.lang.Object[].class, 22);
			kryo.register(java.nio.file.Path.class, 23);
			kryo.register(java.util.ArrayList.class, 24);
			kryo.register(java.util.concurrent.ConcurrentHashMap.class, 25);
			kryo.register(java.util.HashSet.class, 26);
			return kryo;
		}
	};

	private static class InterfaceAwareClassResolver extends DefaultClassResolver {
		@Override
		public Registration getRegistration(Class type) {
			Registration registration = super.getRegistration(type);
			if (registration == null) {
				if (Path.class.isAssignableFrom(type))
					registration = super.getRegistration(Path.class);
				else if (ImmutableSet.class.isAssignableFrom(type))
					registration = super.getRegistration(ImmutableSet.class);
			}
			return registration;
		}
	}

	public static Kryo get() {
		return kryos.get();
	}
}
