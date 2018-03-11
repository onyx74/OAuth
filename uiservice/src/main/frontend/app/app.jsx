import React from "react";
import ReactDom from "react-dom";
import "./src/css/app.css"
class App extends React.Component {

    render() {

        return <div id={"asd"}> <p>Hello12asd, World!</p></div>
    }
}
ReactDom.render(<App />, document.getElementById('react'));