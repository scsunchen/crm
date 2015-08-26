package com.invado.core.domain;

import com.invado.core.dto.EmployeeDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by NikolaB on 6/14/2015.
 */
@NamedQueries({
        @NamedQuery(name = Employee.READ_BY_ORGUNIT, query = "SELECT x FROM Employee x WHERE orgUnit.id = :orgUnit")
})
@Entity
@Table(name = "hr_employee", schema = "devel")
public class Employee implements Serializable {

    public final static String READ_BY_ORGUNIT = "Employee.ReadByOrgUnit";

    @TableGenerator(
            name = "EmployeeTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Employee",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EmployeeTab")
    @Id
    private Integer id;
    @NotNull(message = "{Employee.Name.NotNull}")
    @Column(name = "name")
    private String name;
    @Column(name = "middle_name")
    private String middleName;
    @NotNull(message = "{Employee.LastName.NotNull}")
    @Column(name = "last_name")
    private String lastName;
    @NotNull(message = "{Employee.DateOFBirth.NotNull}")
    @Column(name = "date_of_birth")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "picture")
    private byte[] picture;
    @ManyToOne
    @JoinColumn(name = "org_unit_id", referencedColumnName = "org_unit_id")
    @NotNull(message = "{Employee.OrgUnit.NotNull}")
    private OrgUnit orgUnit;
    @NotNull(message = "{Employee.OrgUnit.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "hire_date")
    private LocalDate hireDate;
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "end_date")
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
    @Embedded
    private Address address;
    @Version
    private Long version;

    public Employee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public EmployeeDTO getDTO(){

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setPicture(this.getPicture());
        employeeDTO.setPhone(this.getPhone());
        employeeDTO.setPostCode(this.getAddress().getPostCode());
        employeeDTO.setCountry(this.getAddress().getCountry());
        employeeDTO.setStreet(this.getAddress().getStreet());
        employeeDTO.setPlace(this.getAddress().getPlace());
        employeeDTO.setDateOfBirth(this.getDateOfBirth());
        employeeDTO.setEmail(this.getEmail());
        employeeDTO.setEndDate(this.getEndDate());
        employeeDTO.setHireDate(this.getHireDate());
        employeeDTO.setId(this.getId());
        employeeDTO.setJobId(this.getJob().getId());
        employeeDTO.setJobName(this.getJob().getName());
        employeeDTO.setLastName(this.getLastName());
        employeeDTO.setDateOfBirth(this.getDateOfBirth());
        employeeDTO.setMiddleName(this.getMiddleName());
        employeeDTO.setName(this.getName());
        employeeDTO.setEndDate(this.getEndDate());
        employeeDTO.setOrgUnitId(this.getOrgUnit().getId());
        employeeDTO.setOrgUnitName(this.getOrgUnit().getName());

        return employeeDTO;
    }

    //************************************************************************//
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (this.orgUnit != other.orgUnit && (this.orgUnit == null || !this.orgUnit.equals(other.orgUnit))) {
            return false;
        }
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.orgUnit != null ? this.orgUnit.hashCode() : 0);
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrgUnit{" + "client=" + orgUnit + ", id=" + id + '}';
    }
}
