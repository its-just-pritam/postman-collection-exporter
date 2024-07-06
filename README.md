<img src="src/main/resources/META-INF/pluginIcon.svg" alt="Logo" width="200"/>

# Postman Collection Exporter

## Overview

This repository contains the source code for a custom IntelliJ IDEA plugin designed to enhance productivity and streamline testing Spring microservices with exposed REST API endpoints. The plugin automatically generates a JSON file by scanning the Project's code base, which can be imported into Postman to obtain a template of all the REST API requests.

## Installation

1. **Download the latest version**
    - [Postman Collection Exporter](https://drive.google.com/file/d/1BBWhDNH5F5f-1hCPhbEDFwB971Y0qplH/view?usp=drive_link)

2. **Install the Plugin**
    - Open IntelliJ IDEA.
    - Select `Navigate` > `Search Everywhere` > type and select `Plugins`.
    - Navigate to the settings > select `Install Plugin from Disk...` > Import the .zip file.

## Features

1. **Export using Tools Menu Popup:**
    - Open Tools menu -> Postman Collection.
    - Select the module(s) or file(s) containing Controller classes.
    - Enter the file name and export path.
    - Select OK.

2. **Export using Project View Popup:**
    - Go to the Project View.
    - Select the module(s) or file(s) containing Controller classes.
    - Right Click -> Postman Collection.
    - Enter the file name and export path.
    - Select OK.
  
3. **Export using Editor Popup:**
    - Open the file containing the Controller class.
    - Right Click -> Postman Collection.
    - Enter the file name and export path.
    - Select OK.

## Contributing

We welcome contributions to improve this plugin! To contribute:

1. **Fork the Repository**
    - Click on the `Fork` button at the top right of this page.

2. **Create a Branch**
    ```sh
    git checkout -b feature/YourFeatureName
    ```

3. **Commit Your Changes**
    ```sh
    git commit -m 'Add some feature'
    ```

4. **Push to the Branch**
    ```sh
    git push origin feature/YourFeatureName
    ```

5. **Create a Pull Request**
    - Open a pull request to the main branch for review.

## Issues

If you encounter any issues or have feature requests, please open an issue in the repository's [issue tracker](https://github.com/its-just-pritam/postman-collection-exporter/issues).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to the IntelliJ IDEA plugin development community for their valuable resources and support.
- Inspired by various open-source plugins and tools.

---

Developed by [Your Name](https://github.com/its-just-pritam)
