package com.navy.docman.hibernate;

/**
 * @Column(name= "<enum column name>", columnDefinition="integer", nullable = true)
 * @Type( type = "org.apps.butler.hibernate.GenericEnumUserType",
 *        parameters = {
 * @Parameter( name = "enumClass", value =
 *             "<real enum class name>"),
 * @Parameter( name = "identifierMethod", value = "value"),
 * @Parameter( name = "valueOfMethod", value = "fromValue") } )
 *
 *             <property name="person_sex"> <type name="org.apps.butler.hibernate.GenericEnumUserType"> <param
 *             name="enumClass"> <real enum class name> </param><param
 *             name="identifierMethod"> value </param><param
 *             name="valueOfMethod"> fromValue </param>
 *             </type> </property>
 *
 *
 * @author xiongll
 *
 * @param <T>
 */

public interface SqlTypeEnum<T extends Enum<T>> {

	public int value();

	public Enum<T> fromValue(int value);

}
