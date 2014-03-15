package ca.ualberta.cs.team5geotopics;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

// taken from https://github.com/zjullion/PicPosterComplete
/**
 * Provides custom base64 serialization / deserialization for Bitmaps. Algorithm
 * taken from:
 * http://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
 * 
 * @author zjullion
 */
public class BitmapJsonConverter implements JsonDeserializer<Bitmap>,
		JsonSerializer<Bitmap> {

	/**
	 * Serializes the provided picture using Json 
	 *
	 * @param  	src	The image source
	 * @param	typeOfSrc	The image type
	 * @param	context	A application context
	 * @return      The image serialized in Json format
	 */
	@Override
	public JsonElement serialize(Bitmap src, Type typeOfSrc,
			JsonSerializationContext context) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		src.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		String base64Encoded = Base64.encodeToString(stream.toByteArray(),
				Base64.NO_WRAP);
		return new JsonPrimitive(base64Encoded);
	}
	
	/**
	 * Deserialize the provided picture from Json 
	 *
	 * @param  	src	The image source in Json format
	 * @param	typeOfSrc	The image type
	 * @param	context	A application context
	 * @return      The image as a bitmap.
	 */
	@Override
	public Bitmap deserialize(JsonElement src, Type typeOfSrc,
			JsonDeserializationContext context) throws JsonParseException {
		String base64Encoded = src.getAsJsonPrimitive().getAsString();
		byte[] data = Base64.decode(base64Encoded, Base64.NO_WRAP);
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
}