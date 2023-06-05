package com.queue.model

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.*

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Settings {
    @JsonProperty("table")
    var table: String? = null
    @JsonProperty("length")
    var length: Int? = null
    @JsonProperty("slider")
    var slider: String? = null
    @JsonProperty("interval")
    var interval: Int? = null
    @JsonProperty("back")
    var backStyle: BackStyle? = null
    @JsonProperty("check")
    var checkStyle: CheckStyle? = null
    @JsonProperty("buttons")
    var buttons: Array<Buttons>? = null
    @JsonProperty("enterprise")
    val enterprise = HashMap<String, String>()
}

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class CheckStyle {
    @JsonProperty("delay")
    var delay: Int = 5
    @JsonProperty("style")
    var style: String = ""
    @JsonProperty("title")
    val title = HashMap<String, String>()
}

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class BackStyle {
    @JsonProperty("style")
    var style: String = ""
    @JsonProperty("title")
    val title = HashMap<String, String>()
}

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Buttons {
    @JsonProperty("id")
    var id: Int = 0
    @JsonProperty("style")
    var style: String = ""
    @JsonProperty("queue")
    var queue: Int = 0
    @JsonProperty("service")
    var service = HashMap<String, String>()
    @JsonProperty("windows")
    val windows: Array<Int> = arrayOf<Int>()
    @JsonProperty("title")
    val title = HashMap<String, String>()
    @JsonProperty("child")
    var child: Array<Childs> = arrayOf<Childs>()
}

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Childs {
    @JsonProperty("id")
    var id: Int = 0
    @JsonProperty("style")
    var style: String = ""
    @JsonProperty("queue")
    var queue: Int = 0
    @JsonProperty("service")
    var service = HashMap<String, String>()
    @JsonProperty("windows")
    val windows: Array<Int> = arrayOf<Int>()
    @JsonProperty("title")
    val title = HashMap<String, String>()
    @JsonProperty("child")
    var child: Array<Childs> = arrayOf<Childs>()
}

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Button (
    var id: Int,
    var title: String,
    var style: String,
    var queue: Int,
    var windows: Array<Int>,
    var service: String,
    var child: Array<Childs>
)