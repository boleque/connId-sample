/*
 * Copyright (c) 2010-2023 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.polygon.connector.training.rest.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazyman on 11/04/2017.
 */

/**
 * Java object representing the USER object type of the remote system. POJO object which is
 * used in the translation from and to the native Json representation of the remote system.
 */
public class RUser extends RObject {


    private String givenName;
    private String familyName;
    private String age;

    private String password;

    /**
     * List of group object id to indicate in which the user object is a member of
     */
    private List<String> memberOf;

    /**
     * Field indicating the activation state of the user object
     */
    private Boolean enabled;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getMemberOf() {
        if (memberOf == null) {
            memberOf = new ArrayList<>();
        }
        return memberOf;
    }

    public void setMemberOf(List<String> memberOf) {
        this.memberOf = memberOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RUser rUser = (RUser) o;

        if (givenName != null ? !givenName.equals(rUser.givenName) : rUser.givenName != null) return false;
        if (familyName != null ? !familyName.equals(rUser.familyName) : rUser.familyName != null) return false;
        if (age != null ? !age.equals(rUser.age) : rUser.age != null) return false;
        if (password != null ? !password.equals(rUser.password) : rUser.password != null) return false;
        if (memberOf != null ? !memberOf.equals(rUser.memberOf) : rUser.memberOf != null) return false;
        return enabled != null ? enabled.equals(rUser.enabled) : rUser.enabled == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (givenName != null ? givenName.hashCode() : 0);
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (memberOf != null ? memberOf.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RUser{");
        sb.append("givenName='").append(givenName).append('\'');
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", memberOf=").append(memberOf);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}
