package org.mustafa.restservice.messenger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mustafa.restservice.messenger.database.DatabaseClass;
import org.mustafa.restservice.messenger.model.Profile;

public class ProfileService {
	
private Map<String, Profile> profiles = DatabaseClass.getProfiles();
	
	public ProfileService(){
		profiles.put("Mustafa", new Profile(1L, "Mustafa", "Mustafa","Bhabhrawala", "http://localhost:8080/messenger/webapi/profiles/Mustafa"));
		profiles.put("Anu", new Profile(2L, "Anu", "Anumol", "Vettathu","http://localhost:8080/messenger/webapi/profiles/Anu"));
		
	}
	
	public List<Profile> getAllProfiles(){
		
		return new ArrayList<Profile>(profiles.values());
	}
	
	public Profile getProfile(String profilename){
		return profiles.get(profilename);
	}
	
	public Profile addProfile(Profile profile){
		profile.setId(profiles.size()+1);
		profile.setUri("http://localhost:8080/messenger/webapi/profiles/"+profile.getProfileName());
		profile.setCreated(new Date());
		profiles.put(profile.getProfileName(),profile);
		return profile;
	}
	
	public Profile updateProfile(Profile profile){
		if(profile.getProfileName().isEmpty()){
			return null;
		}
		profile.setCreated(new Date());
		profiles.put(profile.getProfileName(),profile);
		return profile;
	}
	
	public Profile removeProfile(String profilename){
		return profiles.remove(profilename);
	}

}
