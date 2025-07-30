package org.withtime.be.withtimebe.domain.member.alarm.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.global.data.FirebaseConfigData;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseInitialization {

    private final FirebaseConfigData firebaseConfigData;

    @PostConstruct
    public void initialize() {
        try {
            if (firebaseConfigData.isEnabled()) {
                ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(firebaseConfigData.getConfig().getBytes());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            log.warn("Firebase Initialize", e);
        }
    }
}
