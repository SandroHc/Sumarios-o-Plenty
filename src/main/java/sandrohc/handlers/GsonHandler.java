package sandrohc.handlers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import sandrohc.Main;
import sandrohc.Sumario;

import java.lang.reflect.Type;

public class GsonHandler {
	static Gson gson = new Gson();
	static Type type = new TypeToken<Sumario>() {}.getType();

	public static void deserialize(JsonElement json) throws JsonParseException {
		JsonArray jArr = json.getAsJsonObject().get("sumarios").getAsJsonArray();
		for(short i = 0; i < jArr.size(); i++)
			Main.LISTA.add((Sumario) gson.fromJson(jArr.get(i).getAsJsonObject(), type));
	}

	public static JsonElement serialize() {
		JsonArray array = new JsonArray();
		for(Sumario sum : Main.LISTA)
			array.add(gson.toJsonTree(sum, type));

		JsonObject result = new JsonObject();
		result.add("sumarios", gson.toJsonTree(array));

		return result;
	}
}
