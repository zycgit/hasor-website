/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.website.domain.owner;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.enums.OwnerType;
/**
 * Owner
 * @version : 2016年08月11日
 * @author 赵永春 (zyc@hasor.net)
 */
public class SimpleOwner implements Owner {
    private long      ownerID;
    private String    ownerName;
    private OwnerType ownerType;
    //
    public SimpleOwner() {
    }
    public SimpleOwner(long ownerID, String ownerName, OwnerType ownerType) {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.ownerType = ownerType;
    }
    public SimpleOwner(Owner oriOwner, String ownerName) {
        this.ownerID = oriOwner.getOwnerID();
        this.ownerType = oriOwner.getOwnerType();
        this.ownerName = ownerName;
    }
    //
    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }
    public String getOwnerName() {
        return ownerName;
    }
    @Override
    public long getOwnerID() {
        return this.ownerID;
    }
    @Override
    public OwnerType getOwnerType() {
        return this.ownerType;
    }
    //
    @Override
    public String toString() {
        return this.ownerID + "_" + this.ownerType;
    }
}