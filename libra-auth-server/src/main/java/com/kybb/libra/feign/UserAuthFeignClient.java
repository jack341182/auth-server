package com.kybb.libra.feign;

import com.kybb.common.http.Body;
import com.kybb.solar.user.vo.ModuleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-center-api")
public interface UserAuthFeignClient {
    /**
     * 根据角色获取权限菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("/modules/{roleId}")
    ResponseEntity<Body<List<ModuleVO>>> listByRoleId(@PathVariable("roleId") Long roleId);

    /**
     * 多个角色获取权限菜单
     *
     * @param roleIds
     * @return
     */
    @GetMapping("/modules/list")
    ResponseEntity<Body<List<ModuleVO>>> listByRoleIds(@RequestParam("roleIds") Long[] roleIds);

    /**
     * 根据用户类型获取权限
     * @param userType 用户类型
     * @return
     */
    @GetMapping("/modules/type/{userType}")
    ResponseEntity<Body<List<ModuleVO>>> listByUserType(@PathVariable("userType") Integer userType);

}
