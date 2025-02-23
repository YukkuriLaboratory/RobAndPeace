{
  description = "A basic flake with a shell";
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  inputs.systems.url = "github:nix-systems/default";
  inputs.flake-utils = {
    url = "github:numtide/flake-utils";
    inputs.systems.follows = "systems";
  };

  outputs =
    { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        libraries = with pkgs; lib.makeLibraryPath [ libpulseaudio libGL udev ];
      in
      {
        formatter = pkgs.nixfmt-rfc-style;
        devShells.default = pkgs.mkShell {
            packages = with pkgs; [
                bashInteractive
            ];
            shellHook = ''
                export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${libraries}
                export __GLX_VENDOR_LIBRARY_NAME=mesa
                export MESA_LOADER_DRIVER_OVERRIDE=zink
                export GALLIUM_DRIVER=zink
            '';
        };
      }
    );
}
