package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Shelter;
import re.st.animalshelter.repository.StorageRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {
    private final StorageRepository storageRepository;

    public final static String ANY = "Любой";
    public final static String USER = "Пользователь";
    public final static String OWNER = "Хозяин";

    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public Cell getByCodeAndShelter(Answer answer) {
        return storageRepository.findByCodeAndShelter(answer.getCode(), answer.getShelter()).orElseThrow(RuntimeException::new);
    }

    public Cell getByCodeAndOwner(Answer answer) {
        return storageRepository.findByCodeAndOwner(answer.getCode(), answer.isOwner()).orElseThrow(RuntimeException::new);
    }

    public Cell getByCode(String code) {
        return storageRepository.findByCode(code).orElseThrow(RuntimeException::new); //TODO
    }

    public void saveInformation(String code, Shelter shelter, String person, String text, MultipartFile file) {
        boolean owner = person.equals(OWNER);
        Cell cell;
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
        Optional<Cell> optional = storageRepository.findByCodeAndShelterAndOwner(code, shelter, owner);
        if (optional.isPresent()) {
            cell = optional.get();
            cell.setText(text);
            cell.setPhoto(bytes);
        } else {
            cell = new Cell(code, shelter, owner, text, bytes);
        }
        storageRepository.save(cell);
    }
}
