/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pegov.reports.employee;

import java.util.Objects;

/**
 *
 * @author Андрей
 */
public class EmployeeModel {
    private String name;
    private int countTT = 0;
    private int repeatedTT = 0;
    private int countL3TT = 0;
    private TTComplexity complexity = new TTComplexity();

    public EmployeeModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCountTT() {
        return countTT;
    }

    public int getRepeatedTT() {
        return repeatedTT;
    }

    public int getCountL3TT() {
        return countL3TT;
    }
    
    public int getComplexityLow(){
        return complexity.getLowCount();
    }
    
    public int getComplexityMedium(){
        return complexity.getMediumCount();
    }

    public int getComplexityHight(){
        return complexity.getHightCount();
    }
    
    public void setComplexityString(String complexityStr){
        complexity.count(complexityStr);
    }
    
    public void incRepeatedTT(){
        repeatedTT++;
    }
    
    public void incCountTT(){
        countTT++;
    }
    
    public void incCountL3TT(){
        countL3TT++;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + this.countTT;
        hash = 83 * hash + this.repeatedTT;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmployeeModel other = (EmployeeModel) obj;
        if (this.countTT != other.countTT) {
            return false;
        }
        if (this.repeatedTT != other.repeatedTT) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EmployeeModel{" + "name=" + name + ", countTT=" + countTT + ", repeatedTT=" + repeatedTT + '}';
    }
    
    
}
