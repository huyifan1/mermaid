package com.uboxol.cloud.mermaid.db.repository.kefu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.kefu.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByTransactionIdAndFlagOrderByNumAsc(String transactionId,String flag);
	
	Image findByTransactionIdAndImageIdAndFlag(String transactionId,String imageId,String flag);
}
