package com.gft.akka.common;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


// TODO: Auto-generated Javadoc
/**
 * The Class UserProtocol.
 */

public class Protocol {

  /**
   * The Class UserBasicDataMessage.
   */
  @Getter
  @Setter
  public static class UserBasicDataMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3111241733251173541L;

    /** The id. */
    private String id;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The email. */
    private String email;

    /**
     * Instantiates a new user basic data message.
     *
     * @param id the id
     * @param firstName the first name
     * @param lastName the last name
     * @param email the email
     */
    public UserBasicDataMessage(String id, String firstName, String lastName, String email) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
    }

  

  }

  /**
   * The Class UserPartialDataMessage.
   */
  @Getter
  @Setter
  public static class UserPartialDataMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3845658649639225378L;

    /** The id. */
    private String id;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The email. */
    private String email;

    /** The address. */
    private String address;

    /** The client type. */
    private String clientType;

    /** The job. */
    private String job;

    /** The transfer codes. */
    private List<Integer> transferCodes;

    /** The groups. */
    private List<String> groups;

    /**
     * Instantiates a new user partial data message.
     *
     * @param id the id
     * @param firstName the first name
     * @param lastName the last name
     * @param email the email
     * @param address the address
     * @param clientType the client type
     * @param job the job
     * @param transferCodes the transfer codes
     * @param groups the groups
     */
    public UserPartialDataMessage(String id, String firstName, String lastName, String email, String address, String clientType,
      String job, List<Integer> transferCodes, List<String> groups) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.address = address;
      this.clientType = clientType;
      this.job = job;
      this.transferCodes = transferCodes;
      this.groups = groups;
    }

  

  }

  /**
   * The Class UserSetDataMessage.
   */
  @Getter
  @Setter
  public static class UserSetDataMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7947981473963320554L;
    /** The user set. */
    private List<UserBasicDataMessage> userSet;

    /**
     * Instantiates a new user set data message.
     *
     * @param userSet the user set
     */
    public UserSetDataMessage(List<UserBasicDataMessage> userSet) {
      super();
      this.userSet = userSet;
    }


  }

  /**
   * The Class UserCompleteDataMessage.
   */
  @Getter
  @Setter
  public static class UserCompleteDataMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2270760554343006312L;

    /** The id. */
    private String id;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The email. */
    private String email;

    /** The address. */
    private String address;

    /** The client type. */
    private String clientType;

    /** The job. */
    private String job;

    /** The transfer codes. */
    private List<Integer> transferCodes;

    /** The groups. */
    private List<Group> groups;

    public UserCompleteDataMessage(String id, String firstName, String lastName, String email, String address, String clientType,
      String job, List<Integer> transferCodes, List<Group> groups) {
      super();
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.address = address;
      this.clientType = clientType;
      this.job = job;
      this.transferCodes = transferCodes;
      this.groups = groups;
    }

   

  }

  /**
   * The Class Group.
   */
  @Getter
  @Setter
  public static class Group implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3993966773783372609L;

    /** The name. */
    private String name;

    /** The entities. */
    private List<String> entities;

    /** The transfer codes. */
    private List<Integer> transferCodes;

    /** The active. */
    private Boolean active;

    /**
     * Instantiates a new group.
     *
     * @param name the name
     * @param entities the entities
     * @param transferCodes the transfer codes
     * @param active the active
     */
    public Group(String name, List<String> entities, List<Integer> transferCodes, Boolean active) {
      super();
      this.name = name;
      this.entities = entities;
      this.transferCodes = transferCodes;
      this.active = active;
    }

 

  }

  /**
   * The Class UserFact.
   */
  @Getter
  @Setter
  public static class UserFact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3093461171986125231L;

    /** The user id. */
    private String userId;

    /** The num. */
    private Integer num;

    /** The fac. */
    private BigInteger fac;

    /**
     * Instantiates a new user fact.
     *
     * @param userId the user id
     * @param num the num
     * @param fac the fac
     */
    public UserFact(String userId, Integer num, BigInteger fac) {
      super();
      this.userId = userId;
      this.num = num;
      this.fac = fac;
    }


  }

  /**
   * The Class NumCodeFact.
   */
  @Getter
  @Setter
  public static class NumCodeFact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5812597289469230582L;

    /** The num. */
    private Integer num;

    /** The fac. */
    private BigInteger fac;

 

    /**
     * Instantiates a new num code fact.
     *
     * @param num the num
     * @param fac the fac
     */
    public NumCodeFact(Integer num, BigInteger fac) {
      super();
      this.num = num;
      this.fac = fac;
    }

  }

  /**
   * The Class Fact.
   */
  @Getter
  @Setter
  public static class Fact implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 250271927003743381L;

    /** The user id. */
    private String userId;

    /** The fact. */
    private List<NumCodeFact> fact;

    /**
     * Instantiates a new fact.
     *
     * @param userId the user id
     * @param fact the fact
     */
    public Fact(String userId, List<NumCodeFact> fact) {
      super();
      this.userId = userId;
      this.fact = fact;
    }

  

  }

  /**
   * The Class Facts.
   */
  @Getter
  @Setter
  public static class Facts implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7834527586377836502L;
    /** The facts. */
    private List<Fact> facts;

    /**
     * Instantiates a new facts.
     *
     * @param facts the facts
     */
    public Facts(List<Fact> facts) {
      super();
      this.facts = facts;
    }

  

  }

  public static class GetDictionary implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6049666958884469479L;
  }

  public static class NoUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 17222162274414957L;
  }

  @Getter
  @Setter
  public static class DictionaryRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2806987061286948853L;
    private String userId;

    public DictionaryRequest(String userId) {
      super();
      this.userId = userId;
    }


  }

}
