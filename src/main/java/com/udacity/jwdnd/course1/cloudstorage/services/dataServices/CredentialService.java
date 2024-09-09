package com.udacity.jwdnd.course1.cloudstorage.services.dataServices;

import com.udacity.jwdnd.course1.cloudstorage.data.Credential;
import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;

    public void addCredential(Credential credential){


        this.credentialMapper.insertCredential(credential);

    }

    public Credential getCredentialById(Long  credentialid){ return this. credentialMapper.getCredential(credentialid); }

    public void updateCredential(Credential credential){ this.credentialMapper.updateCredential(credential); }


    public List<Credential> getCredentials(Long userId){
        return this.credentialMapper.getCredentials(userId);
    }

    public void deleteCredential(Long credntialid){ this.credentialMapper.deleteCredential(credntialid);}
}
