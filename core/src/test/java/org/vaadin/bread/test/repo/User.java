package org.vaadin.bread.test.repo;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Alejandro Duarte
 */
@Entity
public class User {

    @NotNull
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @Past
    private LocalDate birthDate;

    @NotNull
    private Integer phoneNumber;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    private Boolean active = true;
    
    private boolean testBool;
    private int testInt;
    private long testLong;
    private float testFloat;
    private double testDouble;
    private char testChar;
    private Integer testIntObj;
    private Long testLongObj;
    private Float testFloatObj;
    private Double testDoubleObj;
    private BigInteger testBigInteger;
    private BigDecimal testBigDecimal;
    
	@ManyToOne
    private Group mainGroup;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Group> groups = new HashSet<>();

    private Gender gender;

    public User() {
    }

    public User(Long id, String name, LocalDate birthDate, String email, Integer phoneNumber , String password, Boolean active, Group mainGroup, Set<Group> groups) {
        this();
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.active = active;
        this.mainGroup = mainGroup;
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Group getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(Group mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

	public boolean isTestBool() {
		return testBool;
	}

	public void setTestBool(boolean testBool) {
		this.testBool = testBool;
	}

	public int getTestInt() {
		return testInt;
	}

	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}

	public long getTestLong() {
		return testLong;
	}

	public void setTestLong(long testLong) {
		this.testLong = testLong;
	}

	public float getTestFloat() {
		return testFloat;
	}

	public void setTestFloat(float testFloat) {
		this.testFloat = testFloat;
	}

	public double getTestDouble() {
		return testDouble;
	}

	public void setTestDouble(double testDouble) {
		this.testDouble = testDouble;
	}

	public char getTestChar() {
		return testChar;
	}

	public void setTestChar(char testChar) {
		this.testChar = testChar;
	}

	public Integer getTestIntObj() {
		return testIntObj;
	}

	public void setTestIntObj(Integer testIntObj) {
		this.testIntObj = testIntObj;
	}

	public Long getTestLongObj() {
		return testLongObj;
	}

	public void setTestLongObj(Long testLongObj) {
		this.testLongObj = testLongObj;
	}

	public Float getTestFloatObj() {
		return testFloatObj;
	}

	public void setTestFloatObj(Float testFloatObj) {
		this.testFloatObj = testFloatObj;
	}

	public Double getTestDoubleObj() {
		return testDoubleObj;
	}

	public void setTestDoubleObj(Double testDoubleObj) {
		this.testDoubleObj = testDoubleObj;
	}

	public BigInteger getTestBigInteger() {
		return testBigInteger;
	}

	public void setTestBigInteger(BigInteger testBigInteger) {
		this.testBigInteger = testBigInteger;
	}

	public BigDecimal getTestBigDecimal() {
		return testBigDecimal;
	}

	public void setTestBigDecimal(BigDecimal testBigDecimal) {
		this.testBigDecimal = testBigDecimal;
	}

    public Gender getGender() {
        return gender;
	}

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
