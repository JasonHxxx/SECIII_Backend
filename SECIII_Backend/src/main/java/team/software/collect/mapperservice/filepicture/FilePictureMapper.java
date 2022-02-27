package team.software.collect.mapperservice.filepicture;

import team.software.collect.po.filepicture.FilePicture;

public interface FilePictureMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(FilePicture record);

    int insertSelective(FilePicture record);

    FilePicture selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(FilePicture record);

    int updateByPrimaryKey(FilePicture record);
}