package team.software.collect.mapperservice.filepicture;

import team.software.collect.po.filepicture.FilePicture;

import java.util.List;

public interface FilePictureMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(FilePicture record);

    int insertSelective(FilePicture record);

    FilePicture selectByPrimaryKey(Integer fid);

    FilePicture selectByFileName(String fileName);

    int deleteByFileName(String fileName);

    int updateByPrimaryKeySelective(FilePicture record);

    int updateByPrimaryKey(FilePicture record);

    List<FilePicture> selectByTid(Integer tid);

    List<FilePicture> selectByRid(Integer rid);
}
