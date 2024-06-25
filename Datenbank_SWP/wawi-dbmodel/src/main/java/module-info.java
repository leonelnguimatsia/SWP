module de.thkoeln.swp.wawi.wawidbmodel {

    exports de.thkoeln.swp.wawi.wawidbmodel.entities;
    exports de.thkoeln.swp.wawi.wawidbmodel.exceptions;
    exports de.thkoeln.swp.wawi.wawidbmodel.impl;
    exports de.thkoeln.swp.wawi.wawidbmodel.services;

    requires transitive java.persistence;

    requires java.xml.bind;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires com.h2database;

    opens de.thkoeln.swp.wawi.wawidbmodel.entities;     // TODO mhe Vielleicht mal 'to hibernate' ud schauen was dann fehlt
}
