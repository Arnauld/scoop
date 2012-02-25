package org.technbolts.scoop.data.scrum;

import fj.data.Option;

public enum Role {
    Developer("developer"), //
    ScrumMaster("scrum-master"), //
    ProductOwner("product-owner");
    
    private final String identifier;
    private Role(String identifier) {
        this.identifier = identifier;
    }
    
    public String identifier() {
        return identifier;
    }
    
    public static Option<Role> getByIdentifier(String identifier) {
        for(Role r : values()) {
            if(r.identifier.equalsIgnoreCase(identifier))
                return Option.some(r);
        }
        return Option.none();
    }
}
