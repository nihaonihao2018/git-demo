package com.arthur.service;

import com.arthur.model.hosp.Department;
import com.arthur.vo.hosp.DepartmentQueryVo;
import com.arthur.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
public interface DepartmentService {
    void save(Map<String, Object> map);

    Page<Department> findDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void deleteDepartment(String hoscode, String depcode);


    List<DepartmentVo> getDepTree(String hosCode);

    Department getDepartmentByDepcode(String depcode);

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
