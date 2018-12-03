package com.neo.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neo.yande.entity.Yande;

public interface YandeRepository extends JpaRepository<Yande, Long> {

	Yande findById(Long id);
	//自定义sql查询v
	@Query(value="select * from yande where image_id = ?1" ,nativeQuery=true)
	List<Yande> findByImageId(String imageId);
	
	@Query(value="select o from Yande o where o.imageName like %:nn%")
    List<Yande> findByImageName(@Param("nn") String imageName);
	//更新操作
	@Modifying
	@Query(value="update Yande o set o.hadDownload=:isSuccess where o.imageId =:pId")
	int findByUuidOrAge(@Param("pId") String imageId,@Param("isSuccess")String hadDownload);
    
}