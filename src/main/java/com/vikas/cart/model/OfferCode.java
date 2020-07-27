package com.vikas.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * OfferCode
 */
@Entity
@Table
public class OfferCode {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private String id = null;

    @Column
    @JsonProperty("buyCount")
    private Integer buyCount = 0;

    @Column
    @JsonProperty("getCount")
    private Integer getCount = 0;

    public OfferCode buyCount(Integer buyCount) {
        this.buyCount = buyCount;
        return this;
    }

    public OfferCode id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(value = "")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get buyCount
     *
     * @return buyCount
     **/
    @ApiModelProperty(value = "")
    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public OfferCode getCount(Integer getCount) {
        this.getCount = getCount;
        return this;
    }

    /**
     * Get getCount
     *
     * @return getCount
     **/
    @ApiModelProperty(value = "")
    public Integer getGetCount() {
        return getCount;
    }

    public void setGetCount(Integer getCount) {
        this.getCount = getCount;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfferCode offerCode = (OfferCode) o;
        return Objects.equals(this.buyCount, offerCode.buyCount) &&
                Objects.equals(this.getCount, offerCode.getCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyCount, getCount);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OfferCode {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    buyCount: ").append(toIndentedString(buyCount)).append("\n");
        sb.append("    getCount: ").append(toIndentedString(getCount)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

