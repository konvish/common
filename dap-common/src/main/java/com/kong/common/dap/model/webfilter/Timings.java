package com.kong.common.dap.model.webfilter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by Administrator on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Timings",
        propOrder = {"send", "wait", "receive"}
)
public class Timings {
        protected long send;
        protected long wait;
        protected long receive;

        public Timings() {
        }

        public long getSend() {
                return this.send;
        }

        public void setSend(long value) {
                this.send = value;
        }

        public long getWait() {
                return this.wait;
        }

        public void setWait(long value) {
                this.wait = value;
        }

        public long getReceive() {
                return this.receive;
        }

        public void setReceive(long value) {
                this.receive = value;
        }
}