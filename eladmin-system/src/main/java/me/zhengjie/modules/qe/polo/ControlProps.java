package me.zhengjie.modules.qe.polo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlProps {
 private String prop;
 private String label;
 public void setpropandlabel(String prop,String label){
     this.prop=prop;
     this.label=label;
 }
}
