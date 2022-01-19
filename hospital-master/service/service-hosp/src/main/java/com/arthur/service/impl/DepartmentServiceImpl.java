package com.arthur.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arthur.model.hosp.Department;
import com.arthur.repository.DepartmentRepository;
import com.arthur.service.DepartmentService;
import com.arthur.vo.hosp.DepartmentQueryVo;
import com.arthur.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "departmentService")
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public void save(Map<String, Object> map) {
        String jsonString = JSONObject.toJSONString(map);
        Department department = JSONObject.parseObject(jsonString, Department.class);
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        Department department1=departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        if(null!=department1){
            //修改
            department1.setUpdateTime(new Date());
            department1.setIsDeleted(0);
            departmentRepository.save(department1);
        }else {
            //添加
            department.setUpdateTime(new Date());
            department.setCreateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> findDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page-1, limit,sort);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void deleteDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null!=department){
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> getDepTree(String hosCode) {
        ArrayList<DepartmentVo> result = new ArrayList<>();
        Department department = new Department();
        department.setHoscode(hosCode);
        Example<Department> example = Example.of(department);
        List<Department> all = departmentRepository.findAll(example);
        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        Set<Map.Entry<String, List<Department>>> entrySet = collect.entrySet();
        for(Map.Entry<String, List<Department>> entry:entrySet){
            String bigCode = entry.getKey();
            List<Department> departments = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departments.get(0).getBigname());

            ArrayList<DepartmentVo> departmentVos = new ArrayList<>();
            for(Department dep:departments){
                DepartmentVo departmentVoChild = new DepartmentVo();
                departmentVoChild.setDepcode(dep.getDepcode());
                departmentVoChild.setDepname(dep.getDepname());
                departmentVos.add(departmentVoChild);
            }
            departmentVo.setChildren(departmentVos);
            result.add(departmentVo);
        }

        return result;
    }

    @Override
    public Department getDepartmentByDepcode(String depcode) {
        Department department=departmentRepository.getDepartmentByDepcode(depcode);
        return department;
    }

    @Override
    public Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }


}
