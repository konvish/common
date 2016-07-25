/*
 * Copyright 2014-2015 Jakub Jirutka <jakub@jirutka.cz>.
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
package cz.jirutka.spring.exhandler.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URI;

@JsonInclude(Include.NON_EMPTY)
@JsonSerialize(
        include = Inclusion.NON_EMPTY
)
@XmlRootElement(
        name = "problem"
)
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private URI type;
    private String title;
    private Integer status;
    private String detail;
    private URI instance;

    public ErrorMessage(ErrorMessage orig) {
        this.type = orig.getType();
        this.title = orig.getTitle();
        this.status = orig.getStatus();
        this.detail = orig.getDetail();
        this.instance = orig.getInstance();
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatus(HttpStatus status) {
        this.status = Integer.valueOf(status.value());
    }

    public URI getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getDetail() {
        return this.detail;
    }

    public URI getInstance() {
        return this.instance;
    }

    public void setType(URI type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setInstance(URI instance) {
        this.instance = instance;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof ErrorMessage)) {
            return false;
        } else {
            ErrorMessage other = (ErrorMessage)o;
            if(!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    URI this$type = this.getType();
                    URI other$type = other.getType();
                    if(this$type == null) {
                        if(other$type == null) {
                            break label71;
                        }
                    } else if(this$type.equals(other$type)) {
                        break label71;
                    }

                    return false;
                }

                String this$title = this.getTitle();
                String other$title = other.getTitle();
                if(this$title == null) {
                    if(other$title != null) {
                        return false;
                    }
                } else if(!this$title.equals(other$title)) {
                    return false;
                }

                label57: {
                    Integer this$status = this.getStatus();
                    Integer other$status = other.getStatus();
                    if(this$status == null) {
                        if(other$status == null) {
                            break label57;
                        }
                    } else if(this$status.equals(other$status)) {
                        break label57;
                    }

                    return false;
                }

                String this$detail = this.getDetail();
                String other$detail = other.getDetail();
                if(this$detail == null) {
                    if(other$detail != null) {
                        return false;
                    }
                } else if(!this$detail.equals(other$detail)) {
                    return false;
                }

                URI this$instance = this.getInstance();
                URI other$instance = other.getInstance();
                if(this$instance == null) {
                    if(other$instance == null) {
                        return true;
                    }
                } else if(this$instance.equals(other$instance)) {
                    return true;
                }

                return false;
            }
        }
    }

    public boolean canEqual(Object other) {
        return other instanceof ErrorMessage;
    }

    public int hashCode() {
        byte result = 1;
        URI $type = this.getType();
        int result1 = result * 59 + ($type == null?0:$type.hashCode());
        String $title = this.getTitle();
        result1 = result1 * 59 + ($title == null?0:$title.hashCode());
        Integer $status = this.getStatus();
        result1 = result1 * 59 + ($status == null?0:$status.hashCode());
        String $detail = this.getDetail();
        result1 = result1 * 59 + ($detail == null?0:$detail.hashCode());
        URI $instance = this.getInstance();
        result1 = result1 * 59 + ($instance == null?0:$instance.hashCode());
        return result1;
    }

    public ErrorMessage() {
    }

    public String toString() {
        return "ErrorMessage(type=" + this.getType() + ", title=" + this.getTitle() + ", status=" + this.getStatus() + ", instance=" + this.getInstance() + ")";
    }
}
