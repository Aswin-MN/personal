## kernel-idvalidator-uin

 1- [Background & Design](../../design/kernel/kernel-idvalidator-uin.md)
 

 
 2- [API Documentation ]
 
 ```
 mvn javadoc:javadoc

 ```
 
**Properties to be added in Spring application environment using this component**

[application-dev.properties](../../config/application-dev.properties)

 
 
 **Maven Dependency**
 
 ```
 	<dependency>
			<groupId>io.mosip.kernel</groupId>
			<artifactId>kernel-idvalidator-uin</artifactId>
			<version>${project.version}</version>
		</dependency>

 ```
 

**UIN validation**

1. Input String should be numeric string

2. Length of the given Input is as mentioned in property file for the respective IDs

3. Input Numeric String should not contain '0' or '1' as the first digit

4. Input Numeric String should not contain any sequential number for 2 or more than two digits

5. Input Numeric String should not contain any repeating numbers for 2 or more than two digits

6. and Input Numeric String should not have repeated block of numbers for more than 2 digits

7. The last digit of the generated token id should have checksum.


**The response will be true is case if it pass the all validation condition otherwise it will throw respective error message**

 

**Usage Sample:**

Autowired interface IdValidator

```
	@Autowired
	private UinValidator<String> uinValidatorImpl;
```

Call the method validate Id

 
Valid UIN  Example:
 
 ```
	boolean isValid = uinValidatorImpl.validateId("426789089018"); //return true
	
```

Invalid UIN Example

```
	boolean isValid = uinValidatorImpl.validateId("026789089018"); //throw Exception "UIN should not contain Zero or One as first digit."

 
 ```
