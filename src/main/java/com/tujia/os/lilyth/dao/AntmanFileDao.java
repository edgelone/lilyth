package com.tujia.os.lilyth.dao;

import com.tujia.os.lilyth.model.AntmanFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AntmanFileDao extends JpaRepository<AntmanFile, Integer> {
    List<AntmanFile> findByFileKey(String fileKey);

    List<AntmanFile> findByFileKeyIn(Collection<String> fileKeys);
}
