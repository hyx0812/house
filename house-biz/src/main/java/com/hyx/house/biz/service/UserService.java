package com.hyx.house.biz.service;

import java.util.List;

import com.google.common.collect.Lists;

import com.hyx.house.biz.mapper.UserMapper;
import com.hyx.house.common.utils.BeanHelper;
import com.hyx.house.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hyx.house.common.model.User;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {

  @Resource
  private UserMapper userMapper;

  @Resource
  private FileService fileService;

  @Resource
  private MailService mailService;

  @Value("${file.prefix}")
  private String imgPrefix;


  public List<User> getUsers() {
    return userMapper.selectUsers();
  }

  /**
   * 1. 插入数据库，非激活;密码加盐md5;保存头像到本地
   * 2. 生成key，绑定Email
   * 3. 发送邮件给用户
   * @param account
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean addAccount(User account){
    account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
    List<String> imgList = fileService.getImgPath(Lists.newArrayList(account.getAvatarFile()));
    if (!imgList.isEmpty()){
      account.setAvatar(imgList.get(0));
    }
    BeanHelper.setDefaultProp(account, User.class);
    BeanHelper.onInsert(account);
    account.setEnable(0);
    userMapper.insert(account);
    mailService.registerNotify(account.getEmail());
    return true;
  }


    public boolean enable(String key) {
      return mailService.enable(key);
    }

  /**
   * 用户名密码验证
   * @param username
   * @param password
   * @return
   */
    public User auth(String username, String password) {
      User user = new User();
      user.setEmail(username);
      user.setPasswd(HashUtils.encryPassword(password));
      user.setEnable(1);
      List<User> list = getUserByQuery(user);
      if (! list.isEmpty()){
        return list.get(0);
      }
      return null;
    }

  public List<User> getUserByQuery(User user) {
      List<User> list = userMapper.selectUsersByQuery(user);
      list.forEach(u -> {
        u.setAvatar(imgPrefix + u.getAvatar());
      });
      return list;
  }

  public void resetNotify(String username) {
    mailService.resetNotify(username);
  }

  /**
   * 重置密码操作
   * @param key
   */
  @Transactional(rollbackFor=Exception.class)
  public User reset(String key,String password){
    String email = getResetEmail(key);
    User updateUser = new User();
    updateUser.setEmail(email);
    updateUser.setPasswd(HashUtils.encryPassword(password));
    userMapper.update(updateUser);
    mailService.invalidateRestKey(key);
    return getUserByEmail(email);
  }

  public User getUserByEmail(String email) {
    User queryUser = new User();
    queryUser.setEmail(email);
    List<User> users = getUserByQuery(queryUser);
    if (!users.isEmpty()) {
      return users.get(0);
    }
    return null;
  }

  public String getResetEmail(String key) {
    String email = "";
    try {
      email =  mailService.getResetEmail(key);
    } catch (Exception ignore) {
    }
    return email;
  }

  public void updateUser(User updateUser, String email) {
    updateUser.setEmail(email);
    BeanHelper.onUpdate(updateUser);
    userMapper.update(updateUser);
  }
}
