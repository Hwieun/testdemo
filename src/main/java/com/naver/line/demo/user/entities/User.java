package com.naver.line.demo.user.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.naver.line.demo.account.entity.Account;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String name;

  @Column
  private String email;

  @Column
  private String phone;

  @Column
  private LocalDate birthday;

  @Column
  private Status status;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Account> accounts;

  public User() { }

  public User(String name, String email, String phone, LocalDate birthday) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.birthday = birthday;
    this.status = Status.ENABLED;
  }

  public static enum Status {
    ENABLED, DISABLED
  }

  public boolean isEnabled() {
    return this.status == Status.ENABLED;
  }

  public void disable() {
    this.status = Status.DISABLED;
  }

}
