package model.entities;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import model.services.Loan;
import model.services.Reminder;

public class User {

	private Integer id;
	private String name;
	private String cpf;
	private String dateOfBirth;
	private String numberOfPhone;
	private String address;
	private List<Loan> historic;
	private Reminder reminder;
	public LocalDate unlockDate;

	public User() {
	}

	public User(Integer id, String name, String cpf, String dateOfBirth, String numberOfPhone, String address) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.dateOfBirth = dateOfBirth;
		this.numberOfPhone = numberOfPhone;
		this.address = address;
		this.historic = new ArrayList<Loan>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNumberOfPhone() {
		return numberOfPhone;
	}

	public void setNumberOfPhone(String numberOfPhone) {
		this.numberOfPhone = numberOfPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Loan> getHistoric() {
		return historic;
	}

	public void setHistoric(String jsonHistoric) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).create();
			Type type = new TypeToken<List<Loan>>() {
			}.getType();
			historic = gson.fromJson(jsonHistoric, type);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void addHistoric(Loan loan) {
		if (historic == null)
			historic = new ArrayList<Loan>();

		historic.add(loan);

	}

	public void initializeReminder(int days) {

		reminder = new Reminder(days);
		unlockDate = reminder.unlock();

	}

	public boolean checksReminder() {
		if (unlockDate != null) {
			LocalDate hoje = LocalDate.now();
			return (unlockDate.isBefore(hoje)) ? true : false;
		} else {
			return true;
		}
	}

	public String jsonHistoric() {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting()
					    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
			String json = gson.toJson(getHistoric());
			return json;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}

	@Override
	public String toString() {
		return "Id = " + id + ", Name = " + name + ", Date of Birth = " + dateOfBirth + ", Number of Phone = "
				+ numberOfPhone + ", Address = " + address + ", Historic = " + jsonHistoric();
	}
}

//These classes are necessaries, because Gson unable convert LocalDate himself. Then this conversion is done manually

class LocalDateAdapter implements JsonSerializer<LocalDate> {

	public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
	}
}

class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
	@Override
	public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
	}
}
