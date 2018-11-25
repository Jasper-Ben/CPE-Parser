/*
 * This file is part of CPE Parser.
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
 *
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package us.springett.parsers.cpe;

import us.springett.parsers.cpe.values.Part;
import us.springett.parsers.cpe.util.Convert;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;
import us.springett.parsers.cpe.exceptions.CpeEncodingException;
import us.springett.parsers.cpe.exceptions.CpeValidationException;
import us.springett.parsers.cpe.util.Status;
import us.springett.parsers.cpe.util.Validate;
import us.springett.parsers.cpe.values.LogicalValue;

/**
 * Object representation of a Common Platform Enumeration (CPE).
 *
 * @author Jeremy Long
 */
public class Cpe implements Serializable {

    private static final long serialVersionUID = 5446537216395895498L;

    /**
     * The part type of the CPE.
     */
    private final Part part;
    /**
     * The vendor component of the CPE in the Well Formed format.
     */
    private final String vendor;
    /**
     * The product component of the CPE in the Well Formed format.
     */
    private final String product;
    /**
     * The version component of the CPE in the Well Formed format.
     */
    private final String version;
    /**
     * The update component of the CPE in the Well Formed format.
     */
    private final String update;
    /**
     * The edition component of the CPE in the Well Formed format.
     */
    private final String edition;
    /**
     * The language component of the CPE in the Well Formed format.
     */
    private final String language;
    /**
     * The swEdition component of the CPE in the Well Formed format. Introduced
     * with CPE 2.3.
     */
    private final String swEdition;
    /**
     * The targetSw component of the CPE in the Well Formed format. Introduced
     * with CPE 2.3.
     */
    private final String targetSw;
    /**
     * The targetHw component of the CPE in the Well Formed format. Introduced
     * with CPE 2.3.
     */
    private final String targetHw;
    /**
     * The other component of the CPE in the Well Formed format. Introduced with
     * CPE 2.3.
     */
    private final String other;

    /**
     * Constructs a new immutable CPE object that represents the Well Form Named
     * defined in the CPE 2.3 specification. Specifying <code>null</code> will
     * be set to the default
     * {@link us.springett.parsers.cpe.values.LogicalValue#ANY}. All values
     * passed in must be well formed (i.e. special characters quoted with a
     * backslash).
     *
     * @see <a href="https://cpe.mitre.org/specification/">CPE 2.3</a>
     * @param part the type of entry: application, operating system, or hardware
     * @param vendor the vendor of the CPE entry
     * @param product the product of the CPE entry
     * @param version the version of the CPE entry
     * @param update the update of the CPE entry
     * @param edition the edition of the CPE entry
     * @param language the language of the CPE entry
     * @param swEdition the swEdition of the CPE entry
     * @param targetSw the targetSw of the CPE entry
     * @param targetHw the targetHw of the CPE entry
     * @param other the other of the CPE entry
     * @throws CpeValidationException thrown if one of the CPE entries is
     * invalid
     */
    Cpe(Part part, String vendor, String product, String version,
            String update, String edition, String language, String swEdition,
            String targetSw, String targetHw, String other) throws CpeValidationException {

        validate(vendor, product, version, update, edition, language, swEdition, targetSw, targetHw, other);
        this.part = part;
        this.vendor = vendor;
        this.product = product;
        this.version = version;
        this.update = update;
        this.edition = edition;
        this.language = language;
        this.swEdition = swEdition;
        this.targetSw = targetSw;
        this.targetHw = targetHw;
        this.other = other;
    }

    /**
     * Validates the CPE attributes.
     *
     * @param vendor1 the vendor
     * @param product1 the product
     * @param version1 the version
     * @param update1 the update version
     * @param edition1 the edition
     * @param language1 the language
     * @param swEdition1 the software edition
     * @param targetSw1 the target software
     * @param targetHw1 the target hardware
     * @param other1 the other attribute
     * @throws CpeValidationException thrown if one or more of the attributes
     * are invalid
     */
    private void validate(String vendor1, String product1, String version1,
            String update1, String edition1, String language1, String swEdition1,
            String targetSw1, String targetHw1, String other1) throws CpeValidationException {
        Status status = Validate.component(vendor1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid vendor component: " + status.getMessage());
        }
        status = Validate.component(product1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid product component: " + status.getMessage());
        }
        status = Validate.component(version1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid version component: " + status.getMessage());
        }
        status = Validate.component(update1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid update component: " + status.getMessage());
        }
        status = Validate.component(edition1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid edition component: " + status.getMessage());
        }
        status = Validate.component(language1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid language component: " + status.getMessage());
        }
        status = Validate.component(swEdition1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid swEdition component: " + status.getMessage());
        }
        status = Validate.component(targetSw1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid targetSw component: " + status.getMessage());
        }
        status = Validate.component(targetHw1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid targetHw component: " + status.getMessage());
        }
        status = Validate.component(other1);
        if (!status.isValid()) {
            throw new CpeValidationException("Invalid other component: " + status.getMessage());
        }
    }

    /**
     * <p>
     * Gets the part for the CPE entry.</p>
     * <pre>cpe:2.3:<b>[part]</b>:[vendor]:[product]:[version]:[update]:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the part for the CPE entry
     */
    public Part getPart() {
        return part;
    }

    /**
     * <p>
     * Gets the vendor for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:<b>[vendor]</b>:[product]:[version]:[update]:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the vendor for the CPE entry
     */
    public String getVendor() {
        return Convert.fromWellFormed(vendor);
    }

    /**
     * <p>
     * Gets the product for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:<b>[product]</b>:[version]:[update]:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the product for the CPE entry
     */
    public String getProduct() {
        return Convert.fromWellFormed(product);
    }

    /**
     * <p>
     * Gets the version for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:<b>[version]</b>:[update]:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the version for the CPE entry
     */
    public String getVersion() {
        return Convert.fromWellFormed(version);
    }

    /**
     * <p>
     * Gets the update for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:<b>[update]</b>:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the update for the CPE entry
     */
    public String getUpdate() {
        return Convert.fromWellFormed(update);
    }

    /**
     * <p>
     * Gets the edition for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:<b>[edition]</b>:[language]:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the edition for the CPE entry
     */
    public String getEdition() {
        return Convert.fromWellFormed(edition);
    }

    /**
     * <p>
     * Gets the language for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:[edition]:<b>[language]</b>:[sw_edition]:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the language for the CPE entry
     */
    public String getLanguage() {
        return Convert.fromWellFormed(language);
    }

    /**
     * <p>
     * Gets the software edition for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:[edition]:[language]:<b>[sw_edition]</b>:[target_sw]:[target_hw]:[other]</pre>
     *
     * @return the software edition for the CPE entry
     */
    public String getSwEdition() {
        return Convert.fromWellFormed(swEdition);
    }

    /**
     * <p>
     * Gets the target software environment for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:[edition]:[language]:[sw_edition]:<b>[target_sw]</b>:[target_hw]:[other]</pre>
     *
     * @return the target software environment for the CPE entry.
     */
    public String getTargetSw() {
        return Convert.fromWellFormed(targetSw);
    }

    /**
     * <p>
     * Gets the target hardware environment for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:[edition]:[language]:[sw_edition]:[target_sw]:<b>[target_hw]</b>:[other]</pre>
     *
     * @return the target hardware environment for the CPE entry
     */
    public String getTargetHw() {
        return Convert.fromWellFormed(targetHw);
    }

    /**
     * <p>
     * Gets the other component for the CPE entry.</p>
     * <pre>cpe:2.3:[part]:[vendor]:[product]:[version]:[update]:[edition]:[language]:[sw_edition]:[target_sw]:[target_hw]:<b>[other]</b></pre>
     *
     * @return the other component for the CPE entry
     */
    public String getOther() {
        return Convert.fromWellFormed(other);
    }

    /**
     * Converts the CPE into the CPE 2.2 URI format.
     *
     * @return the CPE 2.2 URI format of the CPE
     * @throws CpeEncodingException thrown if the CPE is not well formed
     */
    public String toCpe22Uri() throws CpeEncodingException {
        StringBuilder sb = new StringBuilder("cpe:/");
        sb.append(Convert.wellFormedToCpeUri(part)).append(":");
        sb.append(Convert.wellFormedToCpeUri(vendor)).append(":");
        sb.append(Convert.wellFormedToCpeUri(product)).append(":");
        sb.append(Convert.wellFormedToCpeUri(version)).append(":");
        sb.append(Convert.wellFormedToCpeUri(update)).append(":");
        //pack the extra fields from CPE 2.3 into the edition field if present
        //when outputing to 2.2 format
        if (!((swEdition.isEmpty() || "*".equals(swEdition))
                && (targetSw.isEmpty() || "*".equals(targetSw))
                && (targetHw.isEmpty() || "*".equals(targetHw))
                && (other.isEmpty() || "*".equals(other)))) {
            sb.append("~")
                    .append(Convert.wellFormedToCpeUri(edition))
                    .append("~")
                    .append(Convert.wellFormedToCpeUri(swEdition))
                    .append("~")
                    .append(Convert.wellFormedToCpeUri(targetSw))
                    .append("~")
                    .append(Convert.wellFormedToCpeUri(targetHw))
                    .append("~")
                    .append(Convert.wellFormedToCpeUri(other))
                    .append(":");
        } else {
            sb.append(Convert.wellFormedToCpeUri(edition)).append(":");
        }
        sb.append(Convert.wellFormedToCpeUri(language));
        return sb.toString().replaceAll("[:]*$", "");
    }

    /**
     * Converts the CPE into the CPE 2.3 Formatted String.
     *
     * @return the CPE 2.3 Formatted String
     */
    public String toCpe23FS() {
        return String.format("cpe:2.3:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s",
                Convert.wellFormedToFS(part),
                Convert.wellFormedToFS(vendor),
                Convert.wellFormedToFS(product),
                Convert.wellFormedToFS(version),
                Convert.wellFormedToFS(update),
                Convert.wellFormedToFS(edition),
                Convert.wellFormedToFS(language),
                Convert.wellFormedToFS(swEdition),
                Convert.wellFormedToFS(targetSw),
                Convert.wellFormedToFS(targetHw),
                Convert.wellFormedToFS(other));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.part);
        hash = 97 * hash + Objects.hashCode(this.vendor);
        hash = 97 * hash + Objects.hashCode(this.product);
        hash = 97 * hash + Objects.hashCode(this.version);
        hash = 97 * hash + Objects.hashCode(this.update);
        hash = 97 * hash + Objects.hashCode(this.edition);
        hash = 97 * hash + Objects.hashCode(this.language);
        hash = 97 * hash + Objects.hashCode(this.swEdition);
        hash = 97 * hash + Objects.hashCode(this.targetSw);
        hash = 97 * hash + Objects.hashCode(this.targetHw);
        hash = 97 * hash + Objects.hashCode(this.other);
        return hash;
    }

    public boolean matches(Cpe target) {
        boolean result = true;
        result &= compareAttributes(this.part, target.part);
        result &= compareAttributes(this.vendor, target.vendor);
        result &= compareAttributes(this.product, target.product);
        result &= compareAttributes(this.version, target.version);
        result &= compareAttributes(this.update, target.update);
        result &= compareAttributes(this.edition, target.edition);
        result &= compareAttributes(this.language, target.language);
        result &= compareAttributes(this.swEdition, target.swEdition);
        result &= compareAttributes(this.targetSw, target.targetSw);
        result &= compareAttributes(this.targetHw, target.targetHw);
        result &= compareAttributes(this.other, target.other);
        return result;
    }

    /**
     * This does not follow the spec precisely because ANY compared to NA is
     * classified as undefined by the spec; however, in this implementation ANY
     * will match NA and return true.
     *
     * @param left
     * @param right
     * @return
     */
    protected static boolean compareAttributes(Part left, Part right) {
        if (left == right) {
            return true;
        } else if (left == Part.ANY) {
            return true;
        }
        return false;
    }

    /**
     * This does not follow the spec precisely because ANY compared to NA is
     * classified as undefined by the spec; however, in this implementation ANY
     * will match NA and return true.
     *
     * @param left
     * @param right
     * @return
     */
    protected static boolean compareAttributes(String left, String right) {
        if (left.equalsIgnoreCase(right)) {
            //1 6 9
            return true;
        } else if (LogicalValue.ANY.getAbbreviation().equals(left)) {
            //2 3 4
            return true;
        } else if (LogicalValue.NA.getAbbreviation().equals(left)) {
            //5 7 8
            return false;
        } else if (LogicalValue.NA.getAbbreviation().equals(right)) {
            //12 16
            return false;
        } else if (LogicalValue.ANY.getAbbreviation().equals(right)) {
            //13 15
            return false;
        }
        //10 11 14 17

        return false;
    }

    

    public boolean matchedBy(Cpe target) {
        return target.matches(this);
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
        final Cpe compareTo = (Cpe) obj;
        if (!Objects.equals(this.vendor, compareTo.vendor)) {
            return false;
        }
        if (!Objects.equals(this.product, compareTo.product)) {
            return false;
        }
        if (!Objects.equals(this.version, compareTo.version)) {
            return false;
        }
        if (!Objects.equals(this.update, compareTo.update)) {
            return false;
        }
        if (!Objects.equals(this.edition, compareTo.edition)) {
            return false;
        }
        if (!Objects.equals(this.language, compareTo.language)) {
            return false;
        }
        if (!Objects.equals(this.swEdition, compareTo.swEdition)) {
            return false;
        }
        if (!Objects.equals(this.targetSw, compareTo.targetSw)) {
            return false;
        }
        if (!Objects.equals(this.targetHw, compareTo.targetHw)) {
            return false;
        }
        if (!Objects.equals(this.other, compareTo.other)) {
            return false;
        }
        if (this.part != compareTo.part) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return toCpe23FS();
    }

}
